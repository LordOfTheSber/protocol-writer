import { Link } from 'react-router-dom';
import { AppProviders } from './providers/AppProviders';
import { AppRoutes } from './routes/AppRoutes';
import './styles/index.css';

export function App() {
  return (
    <AppProviders>
      <div className="app">
        <nav className="app__nav">
          <Link className="app__brand" to="/">
            📝 Protocol Writer
          </Link>
        </nav>
        <main className="app__main">
          <AppRoutes />
        </main>
      </div>
    </AppProviders>
  );
}
