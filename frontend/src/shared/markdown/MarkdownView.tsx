import MarkdownPreview from '@uiw/react-markdown-preview';
import { markdownComponents } from './markdownComponents';

/** Рендер markdown-документа в режиме чтения (с диаграммами). */
export function MarkdownView({ source }: { source: string }) {
  return (
    <MarkdownPreview
      source={source || '_Документ пуст._'}
      components={markdownComponents}
      wrapperElement={{ 'data-color-mode': 'light' }}
      className="markdown-body"
    />
  );
}
