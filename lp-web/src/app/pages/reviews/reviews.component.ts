// src/app/pages/reviews/reviews.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { KeycloakService } from 'keycloak-angular';

interface ReviewResponse {
  id: number;
  courseId: number;
  userId: number | null;
  rating: number;
  title?: string | null;
  comment: string;
  createdAt: string;
  updatedAt: string;
}

interface ReviewCreateRequest {
  courseId: number;
  userId: number | null;
  rating: number;
  title?: string | null;
  comment: string;
}

@Component({
  selector: 'app-reviews',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.css'],
})
export class ReviewsComponent implements OnInit {
  reviews: ReviewResponse[] = [];

  form: ReviewCreateRequest = {
    courseId: 0,
    userId: null,
    rating: 5,
    title: '',
    comment: '',
  };

  loading = false;

  constructor(
    private http: HttpClient,
    private keycloak: KeycloakService      // 👈 usamos Keycloak directo
  ) {}

  ngOnInit(): void {
    this.loadReviews();
  }

  loadReviews(): void {
    this.loading = true;
    this.http.get<ReviewResponse[]>('/reviews').subscribe({
      next: (data) => {
        this.reviews = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando reviews', err);
        this.loading = false;
      },
    });
  }

  submitReview(formRef: any): void {
    if (!this.form.courseId || !this.form.rating || !this.form.comment) {
      alert('courseId, rating y comment son obligatorios');
      return;
    }

    this.http.post<ReviewResponse>('/reviews', this.form).subscribe({
      next: (created) => {
        alert('Review creada con id ' + created.id);
        this.loadReviews();
        this.form.title = '';
        this.form.comment = '';
        formRef.resetForm({
          courseId: this.form.courseId,
          userId: this.form.userId,
          rating: this.form.rating,
          title: '',
          comment: '',
        });
      },
      error: (err) => {
        console.error('Error creando review', err);
        alert('Error creando review (revisa consola)');
      },
    });
  }

  logout(): void {
    this.keycloak.logout(window.location.origin);
  }
}
