import { apiRequest } from '@/shared/api/client';
import type { ImproveMode, ProtocolAnalysis } from '../model/types';

export const aiApi = {
  /** Улучшение текста open-source LLM (Ollama). Может занимать десятки секунд. */
  improve: (text: string, mode: ImproveMode) =>
    apiRequest<{ text: string }>('/ai/improve', { method: 'POST', body: { text, mode } }),

  /** Структурированный AI-анализ протокола. */
  analyze: (id: string) =>
    apiRequest<ProtocolAnalysis>(`/protocols/${id}/analyze`, { method: 'POST' }),
};
