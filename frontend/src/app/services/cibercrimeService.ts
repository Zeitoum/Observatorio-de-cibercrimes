import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, forkJoin, of } from 'rxjs';
import { catchError, switchMap, map } from 'rxjs/operators';
import { CibercrimeResponse } from '../Cibercrime';

@Injectable({ providedIn: 'root' })
export class CibercrimeService {
  private apiUrl =
    'http://cyber-crime-tracker.sa-east-1.elasticbeanstalk.com/v1/legal-process';

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    }),
  };

  constructor(private http: HttpClient) {}

  /** Lista paginada (sem filtro isCyberCrime) — útil se você quiser reutilizar */
  getProcessos(termo: string = '', pagina: number = 1, limite: number = 10): Observable<CibercrimeResponse> {
    let params = new HttpParams()
      .set('pageSize', String(limite))
      .set('pageNumber', String(pagina))      // seu backend estava OK 1-based
      .set('orderBy', 'id ASC');             // como você já usava antes

    if (termo && termo.trim() !== '') {
      params = params.set('search', termo.trim()); // nome do parâmetro de busca do backend
    }

    return this.http.get<CibercrimeResponse>(this.apiUrl, {
      headers: this.httpOptions.headers,
      params,
    });
  }

  // ------------------------- UTILITÁRIOS “FETCH ALL” -------------------------

  /** Carrega TODAS as páginas respeitando ?search= e orderBy, e retorna items concatenados */
  private getAllProcessosWithSearch(search: string = '', pageSize = 100): Observable<any[]> {
    const firstParams = new HttpParams()
      .set('pageSize', String(pageSize))
      .set('pageNumber', '1')        // 1-based
      .set('orderBy', 'id ASC');

    const addSearch = (p: HttpParams) =>
      search && search.trim() !== '' ? p.set('search', search.trim()) : p;

    return this.http.get<any>(this.apiUrl, {
      headers: this.httpOptions.headers,
      params: addSearch(firstParams),
    }).pipe(
      switchMap(firstPage => {
        const totalItems = firstPage?.totalItems ?? 0;
        const items = firstPage?.items ?? [];

        if (!totalItems || totalItems <= pageSize) {
          return of(items);
        }

        const totalPages = Math.ceil(totalItems / pageSize);
        const requests: Observable<any[]>[] = [];

        for (let i = 2; i <= totalPages; i++) {
          let pageParams = new HttpParams()
            .set('pageSize', String(pageSize))
            .set('pageNumber', String(i))
            .set('orderBy', 'id ASC');
          pageParams = addSearch(pageParams);

          requests.push(
            this.http.get<any>(this.apiUrl, {
              headers: this.httpOptions.headers,
              params: pageParams
            }).pipe(
              map(res => res?.items ?? []),
              catchError(() => of([]))
            )
          );
        }

        return forkJoin(requests).pipe(
          map(pages => [...items, ...pages.flat()]),
          catchError(() => of(items))
        );
      }),
      catchError(err => {
        console.error('getAllProcessosWithSearch erro:', err);
        return of([]);
      })
    );
  }

  /** Retorna TODOS os processos (com busca opcional), filtrando somente isCyberCrime=true */
  getAllCyberProcessos(search: string = '', pageSize = 100): Observable<any[]> {
    return this.getAllProcessosWithSearch(search, pageSize).pipe(
      map(items =>
        (items || []).filter((it: any) =>
          it?.isCyberCrime === true || it?.isCybercrime === true
        )
      ),
      catchError(err => {
        console.error('getAllCyberProcessos erro:', err);
        return of([]);
      })
    );
  }
}
