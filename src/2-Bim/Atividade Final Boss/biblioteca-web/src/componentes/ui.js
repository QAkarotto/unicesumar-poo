// Peças da interface: todas são primitivos do Animate UI (Motion) com as classes do projeto por cima.
import { Button } from '@animate-ui/react/primitives/buttons/button';
import {
  Root as Dialog,
  DialogPortal,
  DialogOverlay,
  DialogContent,
  DialogTitle,
  DialogDescription,
} from '@animate-ui/react/primitives/radix-ui/dialog';
import { Dialog as BaseDialog } from '@animate-ui/react/primitives/base-ui/dialog';
import * as Select from '@animate-ui/react/primitives/radix-ui/select';
import { Tooltip, TooltipTrigger, TooltipContent } from '@animate-ui/react/primitives/tooltip';

// Botão com escala no toque (0.97): a interface confirma que ouviu o clique
export function Botao({ className, ...props }) {
  return <Button type="button" hoverScale={1} tapScale={0.97} className={'botao ' + (className || '')} {...props} />;
}

// Popup de confirmação: fundo e cartão animados pelo Dialog do Radix via Animate UI
export function Confirmacao({ aberta, mensagem, onConfirmar, onFechar }) {
  return (
    <Dialog open={aberta} onOpenChange={(abre) => !abre && onFechar()}>
      <DialogPortal>
        <DialogOverlay className="modal-fundo" />
        <DialogContent className="modal" from="bottom">
          <DialogTitle className="modal-titulo">Confirmar</DialogTitle>
          <DialogDescription className="modal-texto">{mensagem}</DialogDescription>
          <div className="acoes">
            <Botao onClick={onConfirmar}>Confirmar</Botao>
            <Botao className="secundario" onClick={onFechar}>
              Voltar
            </Botao>
          </div>
        </DialogContent>
      </DialogPortal>
    </Dialog>
  );
}

// Select do Radix: lista abre com escala a partir do gatilho; opcoes = [[valor, rótulo]]
export function Seletor({ value, onValueChange, placeholder, opcoes, ...props }) {
  return (
    <Select.Root value={value} onValueChange={onValueChange}>
      <Select.Trigger className="seletor" {...props}>
        <Select.Value placeholder={placeholder} />
        <Select.Icon className="seletor-seta">
          <svg viewBox="0 0 16 16" width="14" height="14" aria-hidden="true">
            <path d="M4 6l4 4 4-4" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
          </svg>
        </Select.Icon>
      </Select.Trigger>
      <Select.Portal>
        <Select.Content className="seletor-lista" position="popper" sideOffset={6}>
          <Select.Viewport>
            {opcoes.map(([valor, rotulo]) => (
              <Select.Item key={valor} value={String(valor)} className="seletor-item">
                <Select.ItemText>{rotulo}</Select.ItemText>
                <Select.ItemIndicator className="seletor-marca">
                  <svg viewBox="0 0 16 16" width="14" height="14" aria-hidden="true">
                    <path d="M3 8.5l3 3 7-7" fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" />
                  </svg>
                </Select.ItemIndicator>
              </Select.Item>
            ))}
          </Select.Viewport>
        </Select.Content>
      </Select.Portal>
    </Select.Root>
  );
}

// Tooltip global: o primeiro espera, os vizinhos abrem na hora (provider no App)
export function Dica({ texto, side = 'right', children }) {
  return (
    <Tooltip side={side} sideOffset={10}>
      <TooltipTrigger asChild>{children}</TooltipTrigger>
      <TooltipContent className="dica-flutuante">{texto}</TooltipContent>
    </Tooltip>
  );
}

// Modal do Base UI (Animate UI): escolher entre gerar ou apagar os dados de demonstração
export function ModalDemo({ aberta, onFechar, onGerar, onApagar }) {
  return (
    <BaseDialog.Root open={aberta} onOpenChange={(abre) => !abre && onFechar()}>
      <BaseDialog.Portal>
        <BaseDialog.Backdrop className="modal-fundo" />
        <BaseDialog.Popup className="modal" from="bottom">
          <BaseDialog.Title className="modal-titulo">Dados de demonstração</BaseDialog.Title>
          <BaseDialog.Description className="modal-texto">
            Gera leitores, livros, empréstimos e devoluções aleatórios, marcados com o selo DEMO, ou apaga os que já foram gerados.
          </BaseDialog.Description>
          <div className="acoes">
            <Botao onClick={onGerar}>Gerar dados</Botao>
            <Botao className="secundario" onClick={onApagar}>
              Apagar dados
            </Botao>
            <Botao className="secundario" onClick={onFechar}>
              Voltar
            </Botao>
          </div>
        </BaseDialog.Popup>
      </BaseDialog.Portal>
    </BaseDialog.Root>
  );
}
