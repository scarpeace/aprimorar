"use client";

import { type ReactNode } from "react";
import { createPortal } from "react-dom";

type ModalProps = {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  description?: string;
  children: ReactNode;
  size?: "sm" | "md" | "lg" | "xl";
};

const sizeClasses = {
  sm: "max-w-md",
  md: "max-w-2xl",
  lg: "max-w-4xl",
  xl: "max-w-6xl",
} as const;

export function Modal({ title, description, children, size = "md", isOpen, onClose }: Readonly<ModalProps>) {
  if (!isOpen || typeof document === "undefined") {
    return null;
  }

  return createPortal(
    <div className="modal modal-open" role="presentation">
      <div className={`modal-box border border-base-300 bg-base-100 shadow-2xl ${sizeClasses[size]}`}>
        <div className="flex items-start justify-between gap-4">
          <div>
            <h3 className="text-lg font-bold text-base-content">{title}</h3>
            {description ? <p className="mt-1 text-sm text-base-content/60">{description}</p> : null}
          </div>

          <button type="button" className="btn btn-ghost btn-sm btn-circle" aria-label="Fechar modal" onClick={onClose}>
            ✕
          </button>
        </div>

        <div className="mt-6">{children}</div>
      </div>

      <button type="button" className="modal-backdrop" aria-label="Fechar modal" onClick={onClose} />
    </div>,
    document.body,
  );
}
