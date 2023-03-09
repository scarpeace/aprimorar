import React, { useEffect } from "react";
import Link from "next/link";

const UniversityCardSection = (props) => {
  const { uniCards } = props;

  return (
    <div className="university-card">
      <div className="container">
        <div className="row">
          <div className="col-xl-12 col-lg-12 col-md-12">
            <div className="university-main-wraper d-flex align-items-center">
              <div className="university-course-box">
                <div className="university-card-text text-center">
                  <p>Aprimorando seus conhecimentos</p>
                  <h3>
                    Aprendendo com professores de <span>verdade</span>
                  </h3>
                  <Link href="/course">
                    <a className="edu-six-btn">Serviços</a>
                  </Link>
                </div>
              </div>
              <div className="university-card-wrapper">
                <div className="university-card-icon"></div>
                <div className="university-card-content">
                  <h3>Acompanhamento no Enem</h3>
                  <p>
                    Aulas de primeira com os melhores professores para a
                    preparação do enem
                  </p>
                </div>
              </div>

              <div className="university-card-wrapper">
                <div className="university-card-icon"></div>
                <div className="university-card-content">
                  <h3>Aulas Particulares</h3>
                  <p>
                    Aulas particulares para adolescentes e crianças de todas as
                    idades
                  </p>
                </div>
              </div>

              <div className="university-card-wrapper">
                <div className="university-card-icon"></div>
                <div className="university-card-content">
                  <h3>Atendimento Exclusivo</h3>
                  <p>
                    Buscamos sempre aprovar nossas pestes não importa o quão
                    encapetado seja a cria.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UniversityCardSection;
