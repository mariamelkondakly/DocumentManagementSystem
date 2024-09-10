import React from 'react'
import { NavLink } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'

const HeaderComponent=()=>{
    return(
        <div>
            <header>
                <nav className=''>
                <div>
                <a href='http://localhost:3000' className='navbar-brand'>Document Management Application</a>                
                </div>
                <ul className=''>
                        <li className='nav-item '>
                        <NavLink to="/register" className="nav-link">Register</NavLink>
                    </li>
                        <li className='nav-item'>
                        <NavLink to="/login" className="nav-link">Login</NavLink>
                    </li>

                    </ul>
                </nav>
            </header>
        </div>
    )
}
export default HeaderComponent
