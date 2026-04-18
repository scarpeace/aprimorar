import { Component, type ReactNode } from "react";
import { ErrorCard } from "./error-card";
import { ButtonLink } from "./button";

export class ErrorBoundary extends Component<
  { children?: ReactNode },
  { error: Error | null }
> {
  state: { error: Error | null } = { error: null };

  static getDerivedStateFromError(error: Error) {
    return { error };
  }

  render() {
    if (this.state.error) {
      return (
        <div className="flex flex-col w-full items-center justify-center p-8">
          <ErrorCard
            title="Ops, encontramos um problema, mas calma. Está tudo bem"
            description={`Um erro inesperado paralisou esta tela, tire um print e mande para o suporte.`}
            error={this.state.error.message}
          />
        </div>
      );
    }

    return this.props.children;
  }
}
