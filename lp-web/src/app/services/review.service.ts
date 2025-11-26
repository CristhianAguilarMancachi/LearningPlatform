// src/app/services/review.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReviewCreateRequest {
  courseId: number;
  userId?: number | null;
  rating: number;
  title?: string;
  comment: string;
}

export interface ReviewResponse extends ReviewCreateRequest {
  id: number;
  createdAt: string;
  updatedAt: string;
}

@Injectable({ providedIn: 'root' })
export class ReviewService {
  private baseUrl = 'http://localhost:8081/reviews';

  constructor(private http: HttpClient) {}

  create(review: ReviewCreateRequest): Observable<ReviewResponse> {
    return this.http.post<ReviewResponse>(this.baseUrl, review);
  }

  getAll(): Observable<ReviewResponse[]> {
    return this.http.get<ReviewResponse[]>(this.baseUrl);
  }
}
