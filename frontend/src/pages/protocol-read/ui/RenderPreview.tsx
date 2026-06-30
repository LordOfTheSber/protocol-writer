import { useState } from 'react';
import { Button, Card } from '@/shared/ui';
import { useProtocolRender, type RenderFormat } from '@/entities/protocol';

export function RenderPreview({ id }: { id: string }) {
  const [format, setFormat] = useState<RenderFormat>('MARKDOWN');
  const { data, isLoading } = useProtocolRender(id, format);

  return (
    <Card className="render-preview">
      <div className="render-preview__header">
        <h3>Экспорт</h3>
        <div className="render-preview__formats">
          <Button
            variant={format === 'MARKDOWN' ? 'primary' : 'secondary'}
            onClick={() => setFormat('MARKDOWN')}
          >
            Markdown
          </Button>
          <Button
            variant={format === 'TEXT' ? 'primary' : 'secondary'}
            onClick={() => setFormat('TEXT')}
          >
            Текст
          </Button>
        </div>
      </div>
      <pre className="render-preview__body">{isLoading ? 'Загрузка…' : data}</pre>
    </Card>
  );
}
