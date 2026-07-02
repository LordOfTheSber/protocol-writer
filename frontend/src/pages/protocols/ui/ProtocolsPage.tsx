import { NewProtocolButton } from '@/features/create-protocol';
import { ProtocolList } from '@/widgets/protocol-list';

export function ProtocolsPage() {
  return (
    <div className="page">
      <header className="page__header">
        <h1>Протоколы</h1>
        <NewProtocolButton />
      </header>
      <ProtocolList />
    </div>
  );
}
