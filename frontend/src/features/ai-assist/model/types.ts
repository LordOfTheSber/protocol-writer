/** Режимы улучшения текста (совпадают с enum ImproveMode на бэкенде). */
export const IMPROVE_MODES = ['IMPROVE', 'SHORTEN', 'EXPAND', 'FIX_GRAMMAR'] as const;

export type ImproveMode = (typeof IMPROVE_MODES)[number];

export const IMPROVE_MODE_LABELS: Record<ImproveMode, string> = {
  IMPROVE: 'Улучшить стиль',
  SHORTEN: 'Сократить',
  EXPAND: 'Развернуть',
  FIX_GRAMMAR: 'Исправить ошибки',
};

/** Структурированный результат AI-анализа протокола. */
export interface ProtocolAnalysis {
  summary: string;
  strengths: string[];
  issues: string[];
  suggestions: string[];
  actionItems: string[];
}
