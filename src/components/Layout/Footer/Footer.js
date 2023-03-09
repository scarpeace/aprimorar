import React from "react";
import Link from "next/link";

const Footer = () => {
  return (
    <footer>
      <div className="footer container">
        <div className="footer-main">
          <div className="row mb-20">
            <div className="col-xxl-8 col-xl-8 col-lg-8 col-md-8 col-sm-8 text-center">
              <div className="footer-logo mt-40 ">
                <Link href="/">
                  <a>
                    <img
                      src="assets/img/logo/logo-black.png"
                      alt="image not found"
                    />
                  </a>
                </Link>
                <div className="university-footer-widget-body">
                  <p className="">
                    Great lesson ideas and lesson plans for ESL teachers!
                    <br />
                    Educators can customize lessons as best plans to knowledge.
                  </p>
                </div>
              </div>
            </div>

            <div className="col-xxl-4 col-xl-4 col-lg-4 col-md-4 col-sm-4 text-center">
              <h4 className="university-footer-widget-title">Browse Courses</h4>

              <div className="university-footer-widget-body">
                <div className="university-footer-link">
                  <ul>
                    <li>
                      <Link href="/course">
                        <a>Development</a>
                      </Link>
                    </li>
                    <li>
                      <Link href="/course">
                        <a>Business</a>
                      </Link>
                    </li>
                    <li>
                      <Link href="/course">
                        <a>Health and Fitness</a>
                      </Link>
                    </li>
                    <li>
                      <Link href="/course">
                        <a>Life Styles</a>
                      </Link>
                    </li>
                    <li>
                      <Link href="/course">
                        <a>Photography</a>
                      </Link>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Linha de fundo rodapé */}

      <div className="university-sub-footer px-30">
        <div className="row pl-40 pr-40 pt-20 pb-20">
          <div className="col-xl-4 col-lg-4 col-md-5 ">
            <div className="sub-footer-text">
              <span>
                © Copyrighted and Designed by{" "}
                <a href="https:www.google.com.br">Scarpellini</a>
              </span>
            </div>
          </div>
          <div className="col-xl-8 col-lg-8 col-md-7">
            <div className="sub-footer-link">
              <ul>
                <li>
                  <Link href="/contact">
                    <a>Privacy Policy</a>
                  </Link>
                </li>
                <li>
                  <Link href="/contact">
                    <a>Terms and Condition</a>
                  </Link>
                </li>
                <li>
                  <Link href="/contact">
                    <a>Sitemap</a>
                  </Link>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
