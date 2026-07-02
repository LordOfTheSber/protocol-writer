import { Route, Routes } from 'react-router-dom';
import { ProtocolsPage } from '@/pages/protocols';
import { ProtocolReadPage } from '@/pages/protocol-read';
import { CreateProtocolPage, EditProtocolPage } from '@/pages/protocol-edit';

export function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<ProtocolsPage />} />
      <Route path="/protocols/new" element={<CreateProtocolPage />} />
      <Route path="/protocols/:id" element={<ProtocolReadPage />} />
      <Route path="/protocols/:id/edit" element={<EditProtocolPage />} />
    </Routes>
  );
}
