import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './footer.html',
  styleUrls: ['./footer.css']
})
export class Footer {
  year = new Date().getFullYear();

  scrollTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}
