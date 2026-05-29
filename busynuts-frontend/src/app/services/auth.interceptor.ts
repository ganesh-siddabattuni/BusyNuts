import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Look in the browser's storage for the logged-in user
  const userString = localStorage.getItem('currentUser');
  let token = null;
  
  if (userString) {
    token = JSON.parse(userString).token; // Grab the exact token string
  }

  // 2. If a token exists, clone the HTTP request and attach the Bearer header
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    // Send the modified request to Spring Boot
    return next(authReq); 
  }

  // 3. If they aren't logged in, just send the normal request (e.g., for public pages)
  return next(req);
};