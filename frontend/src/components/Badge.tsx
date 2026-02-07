import type { ReactNode } from 'react';
import { cn } from '../utils/cn';

type BadgeVariant = 'success' | 'warning' | 'error' | 'info' | 'neutral';

const variantStyles: Record<BadgeVariant, string> = {
  success: 'bg-emerald-50 text-emerald-700 border-emerald-200',
  warning: 'bg-amber-50 text-amber-700 border-amber-200',
  error: 'bg-red-50 text-red-700 border-red-200',
  info: 'bg-blue-50 text-blue-700 border-blue-200',
  neutral: 'bg-slate-50 text-slate-600 border-slate-200',
};

const statusVariantMap: Record<string, BadgeVariant> = {
  ACTIVE: 'success',
  PAID: 'success',
  SUCCEEDED: 'success',
  DELIVERED: 'success',
  TRIALING: 'info',
  OPEN: 'info',
  PENDING: 'info',
  PAST_DUE: 'warning',
  CANCELED: 'neutral',
  DRAFT: 'neutral',
  EXPIRED: 'neutral',
  VOID: 'error',
  FAILED: 'error',
  REFUNDED: 'warning',
};

interface BadgeProps {
  children: ReactNode;
  variant?: BadgeVariant;
  status?: string;
  className?: string;
}

export function Badge({ children, variant, status, className }: BadgeProps) {
  const resolved = variant ?? (status ? statusVariantMap[status] : undefined) ?? 'neutral';

  return (
    <span
      className={cn(
        'inline-flex items-center px-2 py-0.5 rounded-md text-xs font-medium border',
        variantStyles[resolved],
        className
      )}
    >
      {children}
    </span>
  );
}
