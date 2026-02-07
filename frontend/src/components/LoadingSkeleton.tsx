import { cn } from '../utils/cn';

interface LoadingSkeletonProps {
  rows?: number;
  className?: string;
}

export function LoadingSkeleton({ rows = 5, className }: LoadingSkeletonProps) {
  return (
    <div className={cn('space-y-3', className)}>
      {Array.from({ length: rows }, (_, i) => (
        <div key={i} className="animate-pulse flex gap-4">
          <div className="h-4 bg-slate-200 rounded flex-1" style={{ maxWidth: `${60 + Math.random() * 40}%` }} />
          <div className="h-4 bg-slate-200 rounded w-20" />
          <div className="h-4 bg-slate-200 rounded w-16" />
        </div>
      ))}
    </div>
  );
}

export function TableSkeleton({ rows = 5 }: { rows?: number }) {
  return (
    <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
      <div className="border-b border-slate-200 bg-slate-50/80 px-4 py-3">
        <div className="animate-pulse flex gap-8">
          {Array.from({ length: 5 }, (_, i) => (
            <div key={i} className="h-3 bg-slate-200 rounded w-20" />
          ))}
        </div>
      </div>
      <div className="divide-y divide-slate-100 p-4 space-y-4">
        {Array.from({ length: rows }, (_, i) => (
          <div key={i} className="animate-pulse flex gap-8 pt-3">
            {Array.from({ length: 5 }, (_, j) => (
              <div key={j} className="h-4 bg-slate-100 rounded flex-1" />
            ))}
          </div>
        ))}
      </div>
    </div>
  );
}

export function KpiSkeleton() {
  return (
    <div className="grid grid-cols-4 gap-6">
      {Array.from({ length: 4 }, (_, i) => (
        <div key={i} className="bg-white rounded-xl border border-slate-200 p-5 animate-pulse">
          <div className="h-3 bg-slate-200 rounded w-24 mb-3" />
          <div className="h-8 bg-slate-200 rounded w-32 mb-2" />
          <div className="h-3 bg-slate-100 rounded w-16" />
        </div>
      ))}
    </div>
  );
}
