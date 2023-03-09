import React, { useState, useEffect } from "react";
import Link from "next/link";
import { useRouter } from "next/router";
import Image from "next/image";

import useSticky from "../../../hooks/useSticky";
import MobileMenu from "./MobileMenu";
import logo from "../../../../public/assets/img/logo/logo-aprimorar.png";

const HeaderFour = () => {
  // sticky nav
  const { sticky } = useSticky();

  const [menuOpen, setMenuOpen] = useState(false);

  const router = useRouter();
  const [path, setPath] = useState("");
  useEffect(() => {
    setPath(router.pathname);
  }, [router]);

  return (
    <header>
      {/* NavBar and Links*/}
      <div className={sticky && "header-area sticky-header"}>
        <div className="container-fluid">
          <div className="header-main-wrapper">
            <div className="row align-items-center justify-content-between">
              <div className="col-xl-12 col-lg-12">
                <div className="ms-4 header-left d-flex align-items-center justify-content-between">
                  <Link href="/">
                    <a>
                      <Image src={logo} width={130} height={65} alt="logo" />
                    </a>
                  </Link>

                  <div className="main-menu d-none d-lg-block">
                    <nav id="mobile-menu">
                      <ul>
                        <li className="menu-item">
                          <Link href="/">
                            <a>Home</a>
                          </Link>
                        </li>
                        <li className="menu-item">
                          <Link href="/services-aprimorar">
                            <a>Serviços</a>
                          </Link>
                        </li>
                        <li className="menu-item">
                          <Link href="/equipe">
                            <a>Quem Somos</a>
                          </Link>
                        </li>
                        <li className="menu-item">
                          <Link href="/contato">
                            <a>Contato</a>
                          </Link>
                        </li>
                      </ul>
                    </nav>
                  </div>
                  <div className="col-sm-8 col-md-8 d-lg-none">
                    <div className="header-right d-flex align-items-center justify-content-end">
                      <div className="menu-bar d-xl-none ml-20">
                        <a
                          className="side-toggle"
                          href="#!"
                          onClick={() => {
                            setMenuOpen(!menuOpen);
                          }}
                        >
                          <div className="bar-icon">
                            <span></span>
                            <span></span>
                            <span></span>
                          </div>
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <MobileMenu menuOpen={menuOpen} setMenuOpen={setMenuOpen} />
      <div
        onClick={() => setMenuOpen(false)}
        className={
          menuOpen ? "offcanvas-overlay overlay-signin" : "offcanvas-overlay"
        }
      ></div>
    </header>
  );
};

export default HeaderFour;
