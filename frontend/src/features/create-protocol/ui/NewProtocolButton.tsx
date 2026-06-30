import { useNavigate } from 'react-router-dom';
import { Button } from '@/shared/ui';

export function NewProtocolButton() {
  const navigate = useNavigate();
  return <Button onClick={() => navigate('/protocols/new')}>Новый протокол</Button>;
}
