import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgxEchartsDirective } from 'ngx-echarts';
import * as echarts from 'echarts';
import { HttpClient } from '@angular/common/http';
import { CibercrimeService } from '../services/cibercrimeService';

interface MapDatum { name: string; value: number; }

const LIGHT_TEXT = {
  color: '#111827',
  textBorderWidth: 0,
  textBorderColor: 'transparent',
  textShadowBlur: 0,
  textShadowColor: 'transparent'
};
const DARK_TEXT = {
  color: '#E5E7EB',
  textBorderWidth: 0,
  textBorderColor: 'transparent',
  textShadowBlur: 0,
  textShadowColor: 'transparent'
};

type ThemeTokens = {
  text: typeof LIGHT_TEXT;
  axisLine: string;
  splitLine: string;
  tooltipBg: string;
  tooltipTextColor: string;
  labelLineColor: string;
  mapBorder: string;
};

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, NgxEchartsDirective],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home implements OnInit {
  crimesData: any[] = [];
  years: number[] = [];
  selectedYear: number = new Date().getFullYear();

  loading = true;
  error: string | null = null;

  mapChartOptions: any;
  barChartOptions: any;
  pieChartOptions: any;
  rankChartOptions: any;

  pieTopN = 12;
  pieMinPct = 2;

  // tema
  isDark = true;
  get T(): ThemeTokens {
    return this.isDark
      ? {
          text: DARK_TEXT,
          axisLine: 'rgba(255,255,255,.25)',
          splitLine: 'rgba(255,255,255,.12)',
          tooltipBg: 'rgba(17,24,39,.95)',
          tooltipTextColor: '#FFFFFF',
          labelLineColor: 'rgba(229,231,235,.6)',
          mapBorder: '#aaaaaa'
        }
      : {
          text: LIGHT_TEXT,
          axisLine: 'rgba(0,0,0,.28)',
          splitLine: 'rgba(0,0,0,.10)',
          tooltipBg: 'rgba(255,255,255,.98)',
          tooltipTextColor: '#111111',
          labelLineColor: 'rgba(0,0,0,.45)',
          mapBorder: '#666666'
        };
  }

  // ECharts init
  initOpts = { renderer: 'canvas', useDirtyRect: true };

  // ====== Estados (seleção de UF) ======
  states = [
    { uf: 'SP', name: 'São Paulo', geo: '../../assets/maps/geojs-35-mun.json', ready: true },
    { uf: 'RJ', name: 'Rio de Janeiro', geo: '../../assets/maps/geojs-33-mun.json', ready: false },
    { uf: 'MG', name: 'Minas Gerais', geo: '../../assets/maps/geojs-31-mun.json', ready: false },
  ];
  selectedStateUf: 'SP' | 'RJ' | 'MG' = 'SP';
  get state() { return this.states.find(s => s.uf === this.selectedStateUf)!; }

  // controle de carregamento do mapa
  private mapLoaded = false;

  // nomes possíveis no GeoJSON
  geoNamePropCandidates = ['name', 'NM_MUN', 'NM_MUNICIP', 'NM_MUNICIPIO', 'Nome'];

  // aliases (se precisar ajustar nomes)
  nameAliases: Record<string, string> = {
    // 'SAO PAULO (COMARCA DA CAPITAL)': 'SAO PAULO',
  };

  // paleta para o mapa + legenda
  COLORS = {
    high:   '#d73027',
    midHi:  '#fc8d59',
    mid:    '#fee08b',
    low:    '#6baed6',
    none:   '#f0f0f0'
  };

  // limites atuais (preenchidos a cada update do mapa)
  legend: {
    max: number;
    lowMax?: number;
    midMin?: number; midMax?: number;
    mhMin?: number;  mhMax?: number;
    highMin?: number;
  } | null = null;

  constructor(
    private cibercrimeService: CibercrimeService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const saved = localStorage.getItem('theme');
    if (saved === 'light') this.isDark = false;
    if (saved === 'dark')  this.isDark = true;

    // carrega o geojson do estado atual e os dados
    this.loadGeoAndData();
  }

  // troca tema
  toggleTheme(): void {
    this.isDark = !this.isDark;
    localStorage.setItem('theme', this.isDark ? 'dark' : 'light');
    if (this.mapLoaded) this.updateMapChart();
    this.setupBarChart();
    this.updatePieChart();
    this.updateRankChart();
  }

  // ===== carregamento =====
  private loadGeoAndData(): void {
    this.loadStateGeo(this.selectedStateUf);
    this.loadCrimeData();
  }

  private loadStateGeo(uf: string): void {
    const st = this.states.find(s => s.uf === uf);
    if (!st || !st.ready) return;

    this.mapLoaded = false;
    this.http.get<any>(st.geo).subscribe({
      next: (geo) => {
        echarts.registerMap(st.uf, geo);
        this.mapLoaded = true;
        // se já temos dados, re-renderiza o mapa
        if (this.crimesData.length) this.updateMapChart();
      },
      error: (e) => {
        console.error('Falha ao carregar GeoJSON do estado:', e);
        this.error = `Não consegui carregar o mapa de ${st.name}.`;
      }
    });
  }

  onStateChange(): void {
    // evita selecionar estados "em breve"
    const st = this.state;
    if (!st.ready) return;
    this.loadStateGeo(st.uf);
  }

  loadCrimeData(): void {
    this.loading = true;
    this.error = null;

    // usa apenas isCyberCrime === true
    this.cibercrimeService.getAllCyberProcessos().subscribe({
      next: (allItems) => {
        this.crimesData = (allItems || []).filter(
          (i: any) => !!i?.judgmentDate && !!i?.district
        );

        // anos disponíveis
        this.years = Array
          .from(new Set(this.crimesData.map(c => new Date(c.judgmentDate).getFullYear())))
          .sort((a, b) => a - b);

        this.selectedYear = this.years.length ? Math.max(...this.years) : new Date().getFullYear();

        if (this.mapLoaded) this.updateMapChart();
        this.setupBarChart();
        this.updatePieChart();
        this.updateRankChart();
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar dados completos:', err);
        this.error = 'Erro ao carregar dados completos';
        this.loading = false;
      }
    });
  }

  // ===== helpers =====
  private getFeatureName(f: any): string {
    for (const key of this.geoNamePropCandidates) {
      const v = f?.properties?.[key] ?? f?.[key];
      if (typeof v === 'string' && v.trim()) return v;
    }
    return f?.properties?.name || f?.name || '';
  }

  private normalizeName(s: string): string {
    if (!s) return '';
    const s0 = s.trim();
    const aliased = this.nameAliases[s0.toUpperCase()] || s0;
    return aliased
      .normalize('NFD').replace(/\p{Diacritic}/gu, '')
      .replace(/[\.\-']/g, ' ')
      .replace(/\s+/g, ' ')
      .trim()
      .toUpperCase();
  }

  private dataOfYear(year: number) {
    return this.crimesData.filter(c => new Date(c.judgmentDate).getFullYear() === year);
  }

  private countsByDistrict(year: number): Map<string, number> {
    const m = new Map<string, number>();
    for (const it of this.dataOfYear(year)) {
      const key = this.normalizeName(it.district || '');
      if (!key) continue;
      m.set(key, (m.get(key) || 0) + 1);
    }
    return m;
  }

  private countsBySubject(year: number): Map<string, number> {
    const m = new Map<string, number>();
    for (const it of this.dataOfYear(year)) {
      const key = (it.subject || '').trim();
      if (!key) continue;
      m.set(key, (m.get(key) || 0) + 1);
    }
    return m;
  }

  private yearlyCounts(): { year: number; count: number }[] {
    const byYear = new Map<number, number>();
    for (const it of this.crimesData) {
      const y = new Date(it.judgmentDate).getFullYear();
      byYear.set(y, (byYear.get(y) || 0) + 1);
    }
    return Array.from(byYear.entries())
      .map(([year, count]) => ({ year, count }))
      .sort((a, b) => a.year - b.year);
  }

  private buildPieces(maxVal: number) {
    const pieces: any[] = [{ value: 0, color: this.COLORS.none }];
    const legend: any = { max: maxVal };

    if (maxVal <= 4) {
      if (maxVal >= 1) { pieces.push({ min: 1, max: 1, color: this.COLORS.low });   legend.lowMax = 1; }
      if (maxVal >= 2) { pieces.push({ min: 2, max: 2, color: this.COLORS.mid });   legend.midMin = 2; legend.midMax = 2; }
      if (maxVal >= 3) { pieces.push({ min: 3, max: 3, color: this.COLORS.midHi }); legend.mhMin  = 3; legend.mhMax  = 3; }
      if (maxVal >= 4) { pieces.push({ min: 4,        color: this.COLORS.high });    legend.highMin = 4; }
      return { pieces, legend };
    }

    let b1 = Math.max(1, Math.floor(maxVal * 0.25));
    let b2 = Math.max(b1 + 1, Math.floor(maxVal * 0.50));
    let b3 = Math.max(b2 + 1, Math.floor(maxVal * 0.75));

    pieces.push({ min: 1,     max: b1,       color: this.COLORS.low   }); legend.lowMax = b1;
    pieces.push({ min: b1 + 1, max: b2,      color: this.COLORS.mid   }); legend.midMin = b1 + 1; legend.midMax = b2;
    pieces.push({ min: b2 + 1, max: b3,      color: this.COLORS.midHi }); legend.mhMin  = b2 + 1; legend.mhMax  = b3;
    pieces.push({ min: b3 + 1,               color: this.COLORS.high  }); legend.highMin = b3 + 1;

    return { pieces, legend };
  }

  // ===== MAPA =====
  updateMapChart(): void {
    const year = Number(this.selectedYear);
    const dm = this.countsByDistrict(year);

    const geo = (echarts as any).getMap(this.state.uf)?.geoJson;
    const features = geo?.features || [];

    const mapData: MapDatum[] = features.map((f: any): MapDatum => {
      const rawName = this.getFeatureName(f);
      const key = this.normalizeName(rawName);
      return { name: rawName, value: dm.get(key) || 0 };
    });

    const maxVal = Math.max(1, ...mapData.map(d => d.value));
    const { pieces, legend } = this.buildPieces(maxVal);
    this.legend = legend;

    this.mapChartOptions = {
      backgroundColor: 'transparent',
      textStyle: { ...this.T.text },
      tooltip: {
        trigger: 'item',
        formatter: (p: any) => `${p.name}<br/>Processos: <b>${p?.data?.value ?? 0}</b>`,
        backgroundColor: this.T.tooltipBg,
        borderWidth: 0,
        textStyle: { ...this.T.text, color: this.T.tooltipTextColor }
      },
      toolbox: { feature: { saveAsImage: {} } },
      visualMap: { show: false, type: 'piecewise', pieces },
      series: [{
        name: `${this.state.uf} — ${year}`,
        type: 'map',
        map: this.state.uf,
        roam: 'scale',
        scaleLimit: { min: 0.8, max: 8 },
        layoutCenter: ['50%', '52%'],
        layoutSize: '96%',
        // @ts-ignore
        aspectScale: 1,
        zoom: 1.15,
        itemStyle: { borderColor: this.T.mapBorder, borderWidth: 0.6 },
        emphasis: { itemStyle: { borderWidth: 1 } },
        label: { show: false },
        data: mapData
      }]
    };
  }

  // ===== BARRAS =====
  setupBarChart(): void {
    const yearlyCounts = this.yearlyCounts();

    this.barChartOptions = {
      backgroundColor: 'transparent',
      textStyle: { ...this.T.text },
      title: { left: 'center', textStyle: { ...this.T.text, fontWeight: 700 } },
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
        backgroundColor: this.T.tooltipBg,
        borderWidth: 0,
        textStyle: { ...this.T.text, color: this.T.tooltipTextColor }
      },
      grid: { left: 40, right: 20, bottom: 60, top: 60 },
      xAxis: {
        type: 'category',
        name: 'Ano',
        data: yearlyCounts.map(d => d.year),
        axisLabel: { ...this.T.text },
        axisLine:  { lineStyle: { color: this.T.axisLine } },
        axisTick:  { lineStyle: { color: this.T.axisLine } }
      },
      yAxis: {
        type: 'value',
        name: 'Quantidade',
        axisLabel: { ...this.T.text },
        axisLine:  { lineStyle: { color: this.T.axisLine } },
        splitLine: { lineStyle: { color: this.T.splitLine } }
      },
      series: [{
        name: 'Crimes',
        data: yearlyCounts.map(d => d.count),
        type: 'bar',
        label: { show: true, position: 'top', formatter: '{c}', ...this.T.text },
        itemStyle: {
          color: new (echarts as any).graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#0077e6' }
          ])
        }
      }]
    };
  }

  // ===== PIZZA =====
  updatePieChart(): void {
    const year = Number(this.selectedYear);
    const sm = this.countsBySubject(year);

    const entries = Array
      .from(sm.entries())
      .map(([name, value]) => ({ name, value }))
      .sort((a, b) => b.value - a.value);

    let pieData = entries;
    if (entries.length > this.pieTopN) {
      const top = entries.slice(0, this.pieTopN - 1);
      const othersValue = entries.slice(this.pieTopN - 1).reduce((s, e) => s + e.value, 0);
      pieData = [...top, { name: 'Outros', value: othersValue }];
    }

    this.pieChartOptions = {
      backgroundColor: 'transparent',
      textStyle: { ...this.T.text },
      title: { left: 'center', textStyle: { ...this.T.text, fontWeight: 700 } },
      tooltip: {
        trigger: 'item',
        formatter: '{b}<br/>Qtd: {c} ({d}%)',
        backgroundColor: this.T.tooltipBg,
        borderWidth: 0,
        textStyle: { ...this.T.text, color: this.T.tooltipTextColor }
      },
      legend: {
        type: 'scroll',
        orient: 'horizontal',
        bottom: 0,
        left: 'center',
        textStyle: { ...this.T.text },
        pageIconColor: this.isDark ? '#bbbbbb' : '#555555',
        formatter: (name: string) => {
          const item = pieData.find(d => d.name === name);
          return item ? `${name} (${item.value})` : name;
        }
      },
      series: [{
        name: 'Distribuição',
        type: 'pie',
        radius: ['35%', '65%'],
        center: ['50%', '46%'],
        avoidLabelOverlap: true,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: {
          show: true,
          formatter: (p: any) =>
            (p.percent ?? 0) >= this.pieMinPct ? `${p.name}: ${p.percent.toFixed(1)}%` : '',
          ...this.T.text
        },
        labelLine: { show: true, length: 8, length2: 6, lineStyle: { color: this.T.labelLineColor } },
        emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold', ...this.T.text } },
        data: pieData
      }]
    };
  }

  topN = 5;

  // ===== RANKING =====
  private updateRankChart(topN: number = 5): void {
    const year = Number(this.selectedYear);
    const sm = this.countsBySubject(year);

    const sorted = Array.from(sm.entries())
      .sort((a, b) => b[1] - a[1])
      .slice(0, topN);

    const labels = sorted.map(([s]) => s).reverse();
    const values = sorted.map(([, v]) => v).reverse();

    this.rankChartOptions = {
      backgroundColor: 'transparent',
      textStyle: { ...this.T.text },
      title: { left: 'center', textStyle: { ...this.T.text, fontWeight: 700 } },
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
        backgroundColor: this.T.tooltipBg,
        borderWidth: 0,
        textStyle: { ...this.T.text, color: this.T.tooltipTextColor }
      },
      grid: { left: 160, right: 24, top: 56, bottom: 24 },
      xAxis: {
        type: 'value',
        name: 'Qtd',
        axisLabel: { ...this.T.text },
        axisLine:  { lineStyle: { color: this.T.axisLine } },
        splitLine: { lineStyle: { color: this.T.splitLine } }
      },
      yAxis: {
        type: 'category',
        name: 'Assunto',
        data: labels,
        axisLabel: { ...this.T.text }
      },
      series: [{
        type: 'bar',
        data: values,
        label: { show: true, position: 'right', ...this.T.text }
      }]
    };
  }

  // ano mudou
  onYearChange(): void {
    this.selectedYear = Number(this.selectedYear);
    if (this.mapLoaded) this.updateMapChart();
    this.updatePieChart();
    this.updateRankChart();
  }
}
