import { Link, useParams } from 'react-router-dom';
import { useProtocol, useUpdateProtocol, type ProtocolInput } from '@/entities/protocol';
import { ProtocolEditor } from '@/widgets/protocol-editor';

export function EditProtocolPage() {
  const { id = '' } = useParams();
  const { data, isLoading, isError, error } = useProtocol(id);
  const { mutate, isPending, error: saveError } = useUpdateProtocol(id);

  if (isLoading) return <p className="muted">Загрузка…</p>;
  if (isError) return <p className="error">Ошибка: {(error as Error).message}</p>;
  if (!data) return null;

  const initial: ProtocolInput = {
    title: data.title,
    author: data.author,
    status: data.status,
    sections: data.sections,
  };

  return (
    <div className="page">
      <div className="page__toolbar">
        <Link className="btn btn--ghost" to={`/protocols/${id}`}>
          ← Назад
        </Link>
      </div>
      <h1>Редактирование протокола</h1>
      <ProtocolEditor
        initial={initial}
        submitLabel="Сохранить"
        pending={isPending}
        errorMessage={saveError ? (saveError as Error).message : undefined}
        onSubmit={(input) => mutate(input)}
      />
    </div>
  );
}
