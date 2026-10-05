import { inject } from '@angular/core';
import { HttpInterceptorFn, HttpXsrfTokenExtractor } from '@angular/common/http';

export const csrfInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenExtractor = inject(HttpXsrfTokenExtractor);
  const csrfToken = tokenExtractor.getToken();

  if (csrfToken && req.url.startsWith('http://localhost:8080')) {
    req = req.clone({
      setHeaders: {
        'X-XSRF-TOKEN': csrfToken,
      },
    });
  }

  return next(req);
};
