import { AuthGuard } from './auth/auth.guard';
import { Routes } from '@angular/router';
import { ReviewsComponent } from './pages/reviews/reviews.component';

export const routes: Routes = [
  { path: '', redirectTo: 'reviews', pathMatch: 'full' },
  { path: 'reviews', component: ReviewsComponent, canActivate: [AuthGuard] }
];
