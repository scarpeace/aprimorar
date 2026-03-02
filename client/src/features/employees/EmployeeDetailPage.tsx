import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { UserCog, Mail, CreditCard, Shield, CheckCircle } from "lucide-react"
import { useEffect, useState } from "react";
import type { EmployeeResponse } from "@/lib/schemas";
import { employeesApi } from "@/services/api";

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

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [employee, setEmployee] = useState<EmployeeResponse | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {

      if(!id){
        setError("Missing student id")
        setLoading(false)
        return;
      }
  
      const fetchStudent = async () =>{
        try{
          setLoading(true);
          setError(null);
  
          const res = await employeesApi.getById(id);
          setEmployee(res.data);
        }catch (error) {
            console.error("Failed to load student:", error)
          } finally {
            setLoading(false)
          }
      }
        fetchStudent();
      }, [id])
  
      if (loading) return <div>Loading...</div>
      if(error) return <div>{error}</div>
      if(!employee) return <div>Employee not Found</div>

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-green-100">
            <UserCog className="h-6 w-6 text-green-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Employee Details</h1>
            <p className="text-sm text-gray-500">View and manage employee information</p>
          </div>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/employees">
            ← Back to Employees
          </Link>
        </Button>
      </div>

      <div className="grid gap-6 md:grid-cols-2">
        <Card className="border-l-4 border-l-green-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <UserCog className="h-5 w-5 text-green-500" />
              Personal Information
            </CardTitle>
            <CardDescription>Core employee details</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Full Name" value={employee.name} icon={UserCog} />
            <DetailField label="Email Address" value={employee.email} icon={Mail} />
            <DetailField label="Role" value={employee.role} icon={Shield} />
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-orange-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <CreditCard className="h-5 w-5 text-orange-500" />
              Payment & Status
            </CardTitle>
            <CardDescription>PIX and account status</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="PIX Key" value={employee.pix} icon={CreditCard} />
            <DetailField label="Account Status" value="-" icon={CheckCircle} />
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
