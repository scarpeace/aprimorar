export function getParticipantName(id: string, participants: Array<{ id?: string; nome?: string; name?: string }> = []) {
  return participants.find((participant) => participant.id === id)?.nome
    ?? participants.find((participant) => participant.id === id)?.name
    ?? "Participante removido";
}
