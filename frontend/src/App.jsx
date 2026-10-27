import { HashRouter, Routes, Route } from 'react-router-dom'
import { AppProvider } from './context/AppContext'
import Layout from './components/Layout'
import Dashboard from './pages/Dashboard'
import Projects from './pages/Projects'
import Upload from './pages/Upload'
import Overview from './pages/Overview'
import Explorer from './pages/Explorer'
import Chat from './pages/Chat'
import Architecture from './pages/Architecture'
import Docs from './pages/Docs'
import Debug from './pages/Debug'
import Impact from './pages/Impact'
import Settings from './pages/Settings'

export default function App() {
  return (
    <AppProvider>
      <HashRouter>
        <Routes>
          <Route element={<Layout />}>
            <Route index element={<Dashboard />} />
            <Route path="projects" element={<Projects />} />
            <Route path="projects/:id" element={<Overview />} />
            <Route path="upload" element={<Upload />} />
            <Route path="explorer" element={<Explorer />} />
            <Route path="chat" element={<Chat />} />
            <Route path="architecture" element={<Architecture />} />
            <Route path="docs" element={<Docs />} />
            <Route path="debug" element={<Debug />} />
            <Route path="impact" element={<Impact />} />
            <Route path="settings" element={<Settings />} />
          </Route>
        </Routes>
      </HashRouter>
    </AppProvider>
  )
}
