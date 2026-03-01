import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { GraduationCap, Mail, School, MapPin, User, CheckCircle } from "lucide-react"

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

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>()

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between gap-4">
        <div className="flex items-center gap-3">
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-blue-100">
            <GraduationCap className="h-6 w-6 text-blue-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Student Details</h1>
            <p className="text-sm text-gray-500">View and manage student information</p>
          </div>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/students">
            ← Back to Students
          </Link>
        </Button>
      </div>

      <div className="grid gap-6 md:grid-cols-2">
        <Card className="border-l-4 border-l-blue-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <User className="h-5 w-5 text-blue-500" />
              Personal Information
            </CardTitle>
            <CardDescription>Core student details</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="ID" value={id ?? "-"} />
            <DetailField label="Full Name" value="-" icon={User} />
            <DetailField label="Email Address" value="-" icon={Mail} />
            <DetailField label="Activity Type" value="-" icon={GraduationCap} />
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-purple-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <School className="h-5 w-5 text-purple-500" />
              Academic & Status
            </CardTitle>
            <CardDescription>School and enrollment details</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="School" value="-" icon={School} />
            <DetailField label="Address" value="-" icon={MapPin} />
            <DetailField label="Status" value="-" icon={CheckCircle} />
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
