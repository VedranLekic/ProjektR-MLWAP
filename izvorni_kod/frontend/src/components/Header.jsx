import React from "react";
import "./Header.css";
import mldf from "../pictures/mldf.png";
import menu from "../pictures/menu.png";

function Header() {
  return (
    <header className="header">
      <img src={mldf} alt="mldf" className="mldf-icon" />
      <h1>ML Data Formatter</h1>
      <img src={menu} alt="menu" className="menu-button"/>
    </header>
  );
}

export default Header;
