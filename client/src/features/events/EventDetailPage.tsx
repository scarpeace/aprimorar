import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Calendar, User, GraduationCap, Clock, DollarSign } from "lucide-react"
import type { EventResponse } from "@/lib/schemas";
import { useEffect, useState } from "react";
import { eventsApi } from "@/services/api";

//TODO Improve layout on this page/component + Translate labels + Errors to portuguese
function DetailField({ label, value, icon: Icon }: { label: string; value: string; icon?: React.ElementType }) {
  return (
    <div className="flex items-start gap-3 rounded-lg border border-gray-200 bg-white p-4 shadow-sm">
      {Icon && <Icon className="mt-0.5 h-5 w-5 text-gray-400" />}
      <div className="flex-1">
        <p className="text-xs font-medium uppercase tracking-wide text-gray-500">{label}</p>
        <p className="mt-1 text-sm font-semibold text-gray-900">{value}</p>
      </div>
    </div>
  )
}

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [event, setEvent] = useState<EventResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

    useEffect(() => {
   
       if(!id){
         setError("Missing student id")
         setLoading(false)
         return;
       }
   
       const fetchEvent = async () =>{
         try{
           setLoading(true);
           setError(null);

           const res = await eventsApi.getById(Number.parseInt(id));
           setEvent(res.data)
          }catch (error) {
             console.error("Failed to load student:", error)
           } finally {
             setLoading(false)
           }
       }
         fetchEvent();
       }, [id])

       //TODO Move this logic to the backend and return it on the DTO.
      const price = Number(event?.price)
      const teacherPayment = Number(event?.payment)
      const profit = Number.isFinite(price) && Number.isFinite(teacherPayment) ? price - teacherPayment : 0;
          
      const brl = new Intl.NumberFormat("pt-BR", {
        style:"currency",
        currency:"BRL",
        })

       if(loading) return <div>Loading...</div>
       if(error) return <div>{error}</div>
       if(!event) return <div>Event not found</div>


  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-purple-100">
            <Calendar className="h-6 w-6 text-purple-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Event Details</h1>
            <p className="text-sm text-gray-500">View and manage session information</p>
          </div>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/events">
            ← Back to Events
          </Link>
        </Button>
      </div>

      <div className="grid gap-6 md:grid-cols-2">
        <Card className="border-l-4 border-l-purple-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <Calendar className="h-5 w-5 text-purple-500" />
              Detalhes do Atendimento
            </CardTitle>
            <CardDescription>Informações do Evento</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="ID" value={id ?? "-"} />
            //TODO Title in weird position, align left
            <DetailField label="Event Title" value={event.title} icon={Calendar} />
            <DetailField label="Description" value={event.description ?? "-"} />
            //TODO Simplify date format, move logic to backend
            <DetailField label="Data" value={event.startDateTime} icon={Clock} />
            <DetailField label="Horário Início" value={event.startDateTime} icon={Clock} />
            <DetailField label="Horário Fim" value={event.endDateTime} icon={Clock} />
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-indigo-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <User className="h-5 w-5 text-indigo-500" />
              Participants & Payment
            </CardTitle>
            <CardDescription>Students, employees and financial details</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Student" value={event.studentName} icon={GraduationCap} />
            <DetailField label="Teacher" value={event.employeeName} icon={User} />
            <DetailField label="Price" value={brl.format(price)} icon={DollarSign} />
            <DetailField label="Payment Status" value={brl.format(teacherPayment)} icon={DollarSign} />
            <DetailField label="Payment Status" value={brl.format(profit)} icon={DollarSign} />
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
