import { Component, HostListener } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class Header {
  isOpen = false;

  toggle(): void {
    this.isOpen = !this.isOpen;
  }

  // Fecha o menu se redimensionar para desktop
  @HostListener('window:resize')
  onResize() {
    if (window.innerWidth >= 992 && this.isOpen) {
      this.isOpen = false;
    }
  }
}
