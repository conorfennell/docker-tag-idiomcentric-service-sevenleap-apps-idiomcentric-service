import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from '../pages/Home'
import NotFound from '../pages/NotFound'

enum ROUTES {
    HOME = '/',
    NOT_FOUND = '*'
}

const Router = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path={ROUTES.HOME} element={<Home />} />
                <Route path={ROUTES.NOT_FOUND} element={<NotFound />} />
            </Routes>
        </BrowserRouter>
    )
}

export default Router
