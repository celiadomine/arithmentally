import { Directive, Input, OnDestroy, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AppAuthService } from '../service/app.auth.service';

@Directive({
  selector: '[isInRoles]' // 
})
export class IsInRolesDirective implements OnInit, OnDestroy {

  @Input() isInRoles?: string[];
  private stop$ = new Subject<void>();
  private isVisible = false;

  constructor(
    private viewContainerRef: ViewContainerRef,
    private templateRef: TemplateRef<any>,
    private authService: AppAuthService) {
      
    }

  ngOnInit() {
    this.authService.getRoles().pipe(
      takeUntil(this.stop$)
    ).subscribe(roles => {
      if (!roles) {
        this.viewContainerRef.clear();
        return;
      }

      const hasAllRoles = this.isInRoles?.every(r => roles.includes(r)) ?? false;

      if (hasAllRoles && !this.isVisible) {
        this.viewContainerRef.createEmbeddedView(this.templateRef);
        this.isVisible = true;
      } else if (!hasAllRoles && this.isVisible) {
        this.viewContainerRef.clear();
        this.isVisible = false;
      }
    });
  }

  ngOnDestroy() {
    this.stop$.next();
    this.stop$.complete();
  }
}
