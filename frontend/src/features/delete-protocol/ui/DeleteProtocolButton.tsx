import { Button } from '@/shared/ui';
import { useDeleteProtocol } from '@/entities/protocol';

interface Props {
  id: string;
  onDeleted?: () => void;
}

export function DeleteProtocolButton({ id, onDeleted }: Props) {
  const { mutate, isPending } = useDeleteProtocol();

  const handleClick = () => {
    if (!window.confirm('Удалить протокол? Действие необратимо.')) return;
    mutate(id, { onSuccess: onDeleted });
  };

  return (
    <Button variant="danger" onClick={handleClick} disabled={isPending}>
      Удалить
    </Button>
  );
}
