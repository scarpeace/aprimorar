import React, { useState, useEffect } from "react";
import { useRouter } from "next/router";
import Link from "next/link";

const MobileMenu = ({ setMenuOpen, menuOpen }) => {
  const [home, setHome] = useState(false);
  const [courses, setcourses] = useState(false);
  const [pages, setPages] = useState(false);
  const [project, setProject] = useState(false);
  const [blog, setBlog] = useState(false);
  const [instructor, setInstructor] = useState(false);
  const [faqs, setFaqs] = useState(false);
  const [zoom, setZoom] = useState(false);
  const [events, setEvents] = useState(false);

  const router = useRouter();
  const [path, setPath] = useState("");
  useEffect(() => {
    setPath(router.pathname);
  }, [router]);

  const openMobileMenu = (menu) => {
    if (menu == "home") {
      setHome(!home);
      setcourses(false);
      setBlog(false);
      setPages(false);
      setProject(false);
      setInstructor(false);
      setFaqs(false);
      setZoom(false);
      setEvents(false);
    } else if (menu == "courses") {
      setHome(false);
      setcourses(!courses);
      setBlog(false);
      setPages(false);
      setProject(false);
      setInstructor(false);
      setFaqs(false);
      setZoom(false);
      setEvents(false);
    } else if (menu == "pages") {
      setHome(false);
      setcourses(false);
      setBlog(false);
      setProject(false);
      setPages(!pages);
      setInstructor(false);
      setFaqs(false);
      setZoom(false);
      setEvents(false);
    } else if (menu == "project") {
      setHome(false);
      setcourses(false);
      setBlog(false);
      setPages(false);
      setProject(!project);
      setInstructor(false);
      setFaqs(false);
      setZoom(false);
      setEvents(false);
    } else if (menu == "blog") {
      setHome(false);
      setcourses(false);
      setBlog(!blog);
      setPages(false);
      setProject(false);
      setInstructor(false);
      setFaqs(false);
      setZoom(false);
      setEvents(false);
    } else if (menu == "instructor") {
      setHome(false);
      setcourses(false);
      setBlog(false);
      setPages(false);
      setProject(false);
      setInstructor(!instructor);
      setFaqs(false);
      setZoom(false);
      setEvents(false);
    } else if (menu == "zoom") {
      setHome(false);
      setcourses(false);
      setBlog(false);
      setPages(false);
      setProject(false);
      setInstructor(false);
      setFaqs(false);
      setZoom(!zoom);
      setEvents(false);
    } else if (menu == "events") {
      setHome(false);
      setcourses(false);
      setBlog(false);
      setPages(false);
      setProject(false);
      setInstructor(false);
      setFaqs(false);
      setZoom(false);
      setEvents(!events);
    } else if (menu == "faqs") {
      setHome(false);
      setcourses(false);
      setBlog(false);
      setPages(false);
      setProject(false);
      setInstructor(false);
      setFaqs(!faqs);
      setZoom(false);
      setEvents(false);
    }
  };

  return (
    <div className="fix">
      <div className={menuOpen ? "side-info info-open" : "side-info"}>
        <div className="side-info-content">
          {/* Logo + Botão de fechar */}
          <div className="offset-widget offset-logo mb-40">
            <div className="row align-items-center">
              <div className="col-9">
                <Link href="/">
                  <a>
                    <img src="assets/img/logo/logo-black.png" alt="Logo" />
                  </a>
                </Link>
              </div>
              <div className="col-3 text-end">
                <button
                  className="side-info-close"
                  onClick={() => setMenuOpen(false)}
                >
                  <i className="fal fa-times"></i>
                </button>
              </div>
            </div>
          </div>
          <div className="mm-menu mb-30 d-block d-xl-none">
            <ul>
              <li className={home ? "active" : ""}>
                <a
                  onClick={() => {
                    openMobileMenu("home");
                  }}
                >
                  Home
                </a>
              </li>
              <li className={home ? "active" : ""}>
                <a
                  onClick={() => {
                    openMobileMenu("home");
                  }}
                >
                  Serviços
                </a>
              </li>
              <li className={home ? "active" : ""}>
                <a
                  onClick={() => {
                    openMobileMenu("home");
                  }}
                >
                  Quem Somos
                </a>
              </li>
              <li className={home ? "active" : ""}>
                <a
                  onClick={() => {
                    openMobileMenu("home");
                  }}
                >
                  Contato
                </a>
              </li>
            </ul>
          </div>
        </div>
        <div className="offset-widget">
          <div className="header-menu-top-icon">
            <a href="#">
              <i className="fas fa-phone"></i>(61) 99963-9056
            </a>
            <a href="#">
              <i className="fal fa-envelope"></i>denyse@aprimorar.com
            </a>
            <i className="fal fa-map-marker-alt"></i>
            <span>Clube dos Previdenciarios</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MobileMenu;
