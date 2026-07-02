import { Link } from 'react-router-dom';
import { useCreateProtocol } from '@/entities/protocol';
import { ProtocolEditor } from '@/widgets/protocol-editor';

export function CreateProtocolPage() {
  const { mutate, isPending, error } = useCreateProtocol();

  return (
    <div className="page">
      <div className="page__toolbar">
        <Link className="btn btn--ghost" to="/">
          ← К списку
        </Link>
      </div>
      <h1>Новый протокол</h1>
      <ProtocolEditor
        submitLabel="Создать"
        pending={isPending}
        errorMessage={error ? (error as Error).message : undefined}
        onSubmit={(input) => mutate(input)}
      />
    </div>
  );
}
