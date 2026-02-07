import { NavLink } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';
import {
  HomeIcon,
  CreditCardIcon,
  UserGroupIcon,
  DocumentTextIcon,
  GlobeAltIcon,
  UsersIcon,
  Cog6ToothIcon,
  ArrowRightStartOnRectangleIcon,
  CubeIcon,
} from '@heroicons/react/24/outline';
import { cn } from '../utils/cn';

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: HomeIcon },
  { to: '/plans', label: 'Plans', icon: CubeIcon },
  { to: '/customers', label: 'Customers', icon: UserGroupIcon },
  { to: '/invoices', label: 'Invoices', icon: DocumentTextIcon },
  { to: '/webhooks', label: 'Webhooks', icon: GlobeAltIcon },
  { to: '/users', label: 'Users', icon: UsersIcon },
  { to: '/settings', label: 'Settings', icon: Cog6ToothIcon },
];

export function Sidebar() {
  const { user, logout } = useAuth();

  return (
    <aside className="fixed left-0 top-0 bottom-0 w-60 bg-[#0f172a] text-white flex flex-col z-50">
      <div className="px-6 py-5 border-b border-slate-700/50">
        <div className="flex items-center gap-2.5">
          <CreditCardIcon className="h-7 w-7 text-primary-400" />
          <span className="text-lg font-semibold tracking-tight">BillPilot</span>
        </div>
      </div>

      <nav className="flex-1 px-3 py-4 space-y-0.5 overflow-y-auto">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              cn(
                'flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-all duration-150',
                isActive
                  ? 'bg-primary-500/15 text-primary-400'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/60'
              )
            }
          >
            <item.icon className="h-5 w-5 flex-shrink-0" />
            {item.label}
          </NavLink>
        ))}
      </nav>

      <div className="px-3 py-4 border-t border-slate-700/50">
        <div className="px-3 mb-3">
          <p className="text-sm font-medium text-slate-200 truncate">
            {user?.firstName} {user?.lastName}
          </p>
          <p className="text-xs text-slate-500 truncate">{user?.email}</p>
        </div>
        <button
          onClick={logout}
          className="flex items-center gap-3 px-3 py-2 w-full rounded-lg text-sm text-slate-400 hover:text-slate-200 hover:bg-slate-800/60 transition-all duration-150"
        >
          <ArrowRightStartOnRectangleIcon className="h-5 w-5" />
          Sign Out
        </button>
      </div>
    </aside>
  );
}
