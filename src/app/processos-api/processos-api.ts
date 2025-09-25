import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CibercrimeService } from '../services/cibercrimeService';
import { Cibercrime } from '../Cibercrime';

@Component({
  selector: 'app-processos-api',
  standalone: true,
  imports: [CommonModule, NgbPaginationModule, FormsModule],
  templateUrl: './processos-api.html',
  styleUrls: ['./processos-api.css']
})
export class ProcessosApi implements OnInit {
  private allCyber: Cibercrime[] = [];
  cibercrimes: Cibercrime[] = [];

  termoBusca = '';
  paginaAtual = 1;
  itensPorPagina = 10;
  totalItems = 0;

  carregando = false;
  erro: string | null = null;

  expanded: Record<number, boolean> = {};

  constructor(private cibercrimeService: CibercrimeService) {}

  ngOnInit(): void {
    this.fetchAll();
  }

  fetchAll(): void {
    this.carregando = true;
    this.erro = null;

    this.cibercrimeService.getAllCyberProcessos(this.termoBusca, 100).subscribe({
      next: (items) => {
        this.allCyber = items || [];
        this.totalItems = this.allCyber.length;
        this.paginaAtual = 1;
        this.applySlice();
        this.carregando = false;
      },
      error: () => {
        this.allCyber = [];
        this.cibercrimes = [];
        this.totalItems = 0;
        this.carregando = false;
        this.erro = 'Não foi possível carregar os processos. Tente novamente.';
      }
    });
  }

  private applySlice(): void {
    const start = (this.paginaAtual - 1) * this.itensPorPagina;
    const end = start + this.itensPorPagina;
    this.cibercrimes = this.allCyber.slice(start, end);
  }

  onSearch(): void { this.fetchAll(); }
  clearSearch(): void { this.termoBusca = ''; this.fetchAll(); }
  onPageChange(p: number): void { this.paginaAtual = p; this.applySlice(); }
  onPageSizeChange(): void { this.paginaAtual = 1; this.applySlice(); }

  trackById(_: number, item: Cibercrime) { return item.id; }
  toggleEmenta(id: number) { this.expanded[id] = !this.expanded[id]; }
  isExpanded(id: number) { return !!this.expanded[id]; }
  headnoteLen(h?: string) { return (h?.length) ?? 0; }
  min(a: number, b: number) { return Math.min(a, b); }
}
