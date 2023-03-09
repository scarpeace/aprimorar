import React from "react";

const CampusSection = () => {
  return (
    <div className="campus-area fix pt-120 pb-70 ">
      <div className="container">
        <div className="campus-wrapper position-relative">
          <div className="campus-shape-sticker">
            <div className="shape-light">
              <img
                src="assets/img/shape/shape-light.png"
                alt="image not found"
              />
            </div>
            <div className="campus-shape-content">
              <h5>
                O Aprimorar é <span> a Instituição </span>que torna possível a
                todos
              </h5>
            </div>
          </div>
          <div className="campus-shape-1">
            <img src="assets/img/shape/campus-shape-2.png" alt="shape" />
          </div>
          <div className="campus-shape-2">
            <img src="assets/img/shape/student-shape-05.png" alt="shape" />
          </div>
          <div className="row align-items-center">
            <div className="col-xl-5 col-lg-6">
              <div className="compus-content mb-30">
                <div className="section-title mb-30">
                  <h2>
                    Nossas instalações do
                    <span className="down-mark-line-2">Aprimorar</span> são
                    preparadas para receber você.
                  </h2>
                </div>
                <p>
                  Helping employees gain skills and providing career development
                  often take a back seat to business priorities but workplace
                  better right now. Seventy percent of workers think that.{" "}
                </p>
                <ul>
                  <li>
                    <i className="far fa-check"></i>20 Alunos por Turma{" "}
                  </li>
                  <li>
                    <i className="far fa-check"></i>Aulões e Simulados{" "}
                  </li>
                  <li>
                    <i className="far fa-check"></i>Localização Privilegiada
                  </li>
                  <li>
                    <i className="far fa-check"></i>Ambiente acolhedor
                  </li>
                </ul>
              </div>
            </div>
            <div className="col-xl-6 offset-xl-1 col-lg-6">
              <div className="campus-img-wrapper position-relative">
                <div className="campus-shape-3">
                  <img
                    src="assets/img/shape/campus-shape-1.png"
                    alt="image not found"
                  />
                </div>
                <div className="campus-img-1">
                  <img
                    src="assets/img/campus/campus-img-1.png"
                    alt="image not found"
                  />
                </div>
                <div className="campus-img-2">
                  <img
                    src="assets/img/campus/campus-img-2.png"
                    alt="image not found"
                  />
                </div>
                <div className="campus-img-3">
                  <img
                    src="assets/img/campus/campus-img-3.png"
                    alt="image not found"
                  />
                </div>
                <div className="campus-img-4">
                  <img
                    src="assets/img/campus/campus-img-4.png"
                    alt="image not found"
                  />
                </div>
                <div className="campus-img-5">
                  <img
                    src="assets/img/campus/campus-img-5.png"
                    alt="image not found"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CampusSection;
