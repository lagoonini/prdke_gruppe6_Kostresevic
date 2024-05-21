import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HeaderVisibilityService {
  private headerVisible = new BehaviorSubject<boolean>(true);
  headerVisible$ = this.headerVisible.asObservable();

  setHeaderVisible(isVisible: boolean) {
    this.headerVisible.next(isVisible);
  }
}
