import React from "react";
import Link from "next/link";

const MainBannerSection = () => {
  return (
    <div
      className="hero-area-3 hero-height-3"
      style={{ background: "url(assets/img/home/classroom.jpg)" }}
    >
      <div className="container hero-university-container">
        <div className="row justify-content-center">
          <div className="col-xl-8 col-lg-8 text-center">
            <div className="hero-university-text">
              <span>Uma nova experiência em aprender</span>
              <h2>Espaço multidisciplinar de aprendizagem</h2>
            </div>
            <div className="university-btn">
              <Link href="/contact">
                <a className="edu-five-btn">Entre em contato</a>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainBannerSection;
