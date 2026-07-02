import type { ComponentProps, ReactNode } from 'react';
import MarkdownPreview from '@uiw/react-markdown-preview';
import { Mermaid } from './Mermaid';
import { ExcalidrawEmbed } from './ExcalidrawEmbed';

type Components = NonNullable<ComponentProps<typeof MarkdownPreview>['components']>;

// hast-узел может быть «подсвечен» (содержимое разбито на span'ы), поэтому
// сырой текст блока кода собираем рекурсивным обходом, а не из children.
interface HastNode {
  type?: string;
  value?: string;
  children?: HastNode[];
}

function hastToString(node: HastNode | undefined): string {
  if (!node) return '';
  if (node.type === 'text') return node.value ?? '';
  return (node.children ?? []).map(hastToString).join('');
}

interface CodeProps {
  className?: string;
  children?: ReactNode;
  node?: HastNode;
}

/**
 * Кастомные компоненты для рендера markdown: блоки ```mermaid и ```excalidraw
 * превращаются в диаграммы, остальной код рендерится как обычно.
 */
export const markdownComponents = {
  code: ({ className, children, node }: CodeProps) => {
    const lang = /language-(\w+)/.exec(className ?? '')?.[1];

    if (lang === 'mermaid' || lang === 'excalidraw') {
      const value = hastToString(node).replace(/\n$/, '');
      return lang === 'mermaid' ? <Mermaid code={value} /> : <ExcalidrawEmbed json={value} />;
    }
    return <code className={className}>{children}</code>;
  },
} as Components;
