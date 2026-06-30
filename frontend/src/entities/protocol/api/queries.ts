import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import type { ProtocolInput } from '../model/types';
import { protocolApi, type RenderFormat } from './protocolApi';

export const protocolKeys = {
  all: ['protocols'] as const,
  list: () => [...protocolKeys.all, 'list'] as const,
  detail: (id: string) => [...protocolKeys.all, 'detail', id] as const,
  stats: (id: string) => [...protocolKeys.all, 'stats', id] as const,
  render: (id: string, format: RenderFormat) =>
    [...protocolKeys.all, 'render', id, format] as const,
};

export function useProtocolList() {
  return useQuery({ queryKey: protocolKeys.list(), queryFn: protocolApi.list });
}

export function useProtocol(id: string) {
  return useQuery({ queryKey: protocolKeys.detail(id), queryFn: () => protocolApi.getById(id) });
}

export function useProtocolStats(id: string) {
  return useQuery({ queryKey: protocolKeys.stats(id), queryFn: () => protocolApi.stats(id) });
}

export function useProtocolRender(id: string, format: RenderFormat) {
  return useQuery({
    queryKey: protocolKeys.render(id, format),
    queryFn: () => protocolApi.render(id, format),
  });
}

export function useCreateProtocol() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  return useMutation({
    mutationFn: (input: ProtocolInput) => protocolApi.create(input),
    onSuccess: (created) => {
      queryClient.invalidateQueries({ queryKey: protocolKeys.list() });
      navigate(`/protocols/${created.id}`);
    },
  });
}

export function useUpdateProtocol(id: string) {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  return useMutation({
    mutationFn: (input: ProtocolInput) => protocolApi.update(id, input),
    onSuccess: (updated) => {
      queryClient.invalidateQueries({ queryKey: protocolKeys.all });
      navigate(`/protocols/${updated.id}`);
    },
  });
}

export function useDeleteProtocol() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => protocolApi.remove(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: protocolKeys.list() });
    },
  });
}
