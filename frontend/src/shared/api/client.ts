import { API_BASE } from '@/shared/config';

export class ApiError extends Error {
  constructor(
    public readonly status: number,
    message: string,
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

async function parseError(response: Response): Promise<string> {
  try {
    const data = await response.json();
    // Spring возвращает ProblemDetail с полем detail.
    return data.detail ?? data.message ?? response.statusText;
  } catch {
    return response.statusText;
  }
}

interface RequestOptions {
  method?: string;
  body?: unknown;
  /** Если true — ответ возвращается как текст (для /render). */
  asText?: boolean;
}

export async function apiRequest<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const { method = 'GET', body, asText = false } = options;

  const response = await fetch(`${API_BASE}${path}`, {
    method,
    headers: body !== undefined ? { 'Content-Type': 'application/json' } : undefined,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  if (!response.ok) {
    throw new ApiError(response.status, await parseError(response));
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (asText ? await response.text() : await response.json()) as T;
}
