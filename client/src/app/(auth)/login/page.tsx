import { LoginForm } from "@/auth/components/LoginForm";
import { Card, CardHeader, CardTitle } from "@/components/ui/Card";

export default function LoginPage() {
  return (
    <Card className="w-full max-w-md shadow-xl">
      <CardHeader>
        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-success">Aprimorar</p>
          <CardTitle className="mt-2 normal-case">Entrar na sua conta</CardTitle>
          <p className="mt-2 text-sm text-base-content/65">Informe suas credenciais para acessar o sistema.</p>
        </div>
      </CardHeader>

      <LoginForm />
    </Card>
  );
}
