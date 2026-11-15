import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { TuiButton } from '@taiga-ui/core';
import { AppRoutesPagesEnum } from '../../app.routes-pages.enum';

@Component({
  imports: [TuiButton],
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.html',
})
export class PageNotFound {
  private router = inject(Router);

  goBackBtn() {
    this.router.navigate([AppRoutesPagesEnum.PAGE_HOME]);
  }
}
