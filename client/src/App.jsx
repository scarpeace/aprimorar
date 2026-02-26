import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './index.css'

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-gray-50">
        <Routes>
          <Route path="/" element={<div className="p-8">Aprimorar - Dashboard</div>} />
        </Routes>
      </div>
    </BrowserRouter>
  )
}

export default App
