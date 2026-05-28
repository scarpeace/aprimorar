import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { useListUsers } from "@/kubb";
import { formatDateShortYear } from "@/lib/utils/formatter";
import { useMemo, useState } from "react";

import { DeletarUsuarioButton } from "./DeletarUsuarioButton";

const roleLabels: Record<string, string> = {
  ADMIN: "Administrador",
  EMPLOYEE: "Colaborador",
  PARENT: "Responsavel",
  STUDENT: "Aluno",
};

export function UsuariosTable() {
  const [search, setSearch] = useState("");
  const usuariosQuery = useListUsers();

  const usuariosFiltrados = useMemo(() => {
    const term = search.trim().toLowerCase();
    if (!term) {
      return usuariosQuery.data ?? [];
    }

    return (usuariosQuery.data ?? []).filter((usuario) => {
      const username = usuario.username?.toLowerCase() ?? "";
      const role = roleLabels[usuario.role ?? ""]?.toLowerCase() ?? "";
      return username.includes(term) || role.includes(term);
    });
  }, [search, usuariosQuery.data]);

  if (usuariosQuery.isPending) {
    return <LoadingSpinner text="Carregando usuarios..." />;
  }

  if (usuariosQuery.isError) {
    return <ErrorCard title="Nao foi possivel carregar usuarios" error={usuariosQuery.error} />;
  }

  if (!usuariosQuery.data || usuariosQuery.data.length === 0) {
    return (
      <EmptyCard
        title="Nenhum usuario encontrado"
        description="Cadastre o primeiro usuario administrador ou colaborador."
      />
    );
  }

  return (
    <div className="flex flex-col gap-4">
      <ListSearchInput
        className="w-full"
        placeholder="Buscar por e-mail ou perfil"
        ariaLabel="Buscar usuario"
        value={search}
        onChange={setSearch}
      />

      {usuariosFiltrados.length === 0 ? (
        <EmptyCard
          title="Nenhum usuario corresponde a busca"
          description="Tente outro termo para localizar o cadastro desejado."
        />
      ) : (
        <div className="overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
          <table className="table table-zebra bg-base-100">
            <thead className="bg-base-200/80">
              <tr>
                <th className="text-left font-semibold text-base-content/80">E-mail</th>
                <th className="text-left font-semibold text-base-content/80">Perfil</th>
                <th className="text-left font-semibold text-base-content/80">Status</th>
                <th className="text-left font-semibold text-base-content/80">Criado em</th>
                <th className="text-right font-semibold text-base-content/80">Acoes</th>
              </tr>
            </thead>
            <tbody className="whitespace-nowrap">
              {usuariosFiltrados.map((usuario) => (
                <tr key={usuario.id ?? usuario.username}>
                  <td className="font-medium">{usuario.username ?? "-"}</td>
                  <td>{roleLabels[usuario.role ?? ""] ?? usuario.role ?? "-"}</td>
                  <td>
                    <span className={`badge ${(usuario.active ?? true) ? "badge-success" : "badge-ghost"} badge-sm`}>
                      {(usuario.active ?? true) ? "Ativo" : "Inativo"}
                    </span>
                  </td>
                  <td>{usuario.createdAt ? formatDateShortYear(usuario.createdAt) : "-"}</td>
                  <td className="text-right">
                    {usuario.id ? <DeletarUsuarioButton usuarioId={usuario.id} usuarioEmail={usuario.username ?? ""} /> : null}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
