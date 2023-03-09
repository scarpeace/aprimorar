import React from "react";
import Link from "next/link";

const UniversityMessage = () => {
  return (
    <div className="university-message-area pt-110 pb-105">
      <div className="container">
        <div className="row">
          <div className="col-xl-4 col-lg-4 text-center mt-40">
            <div className="section-title ">
              <h2>
                Bem vindo ao <span className="down-mark-line-2">Aprimorar</span>
              </h2>
            </div>
          </div>
          <div className="col-xl-4 col-lg-4">
            <div className="message-profile-text">
              <span>
                Aprimorar ajuda você a conquistar a sua vaga no enem e ganhar
                dinheiro.
              </span>
              <p>
                Transform your life through education to make the most of each
                semester to choose the best major. Prepare for grad school.
                Whatever it is, you can do it here. Explore our digital
                viewbook. Your journey starts here.
              </p>
            </div>
            <div className="message-meta">
              <Link href="/instructor-profile">
                <a>
                  <img src="assets/img/bg/message-meta.png" alt="meta-img" />
                </a>
              </Link>
              <div className="message-meta-link">
                <Link href="/instructor-profile">
                  <a>
                    <h4>Denyse Braatz</h4>
                  </a>
                </Link>
                <p>Fundadora, Aprimorar</p>
              </div>
            </div>
          </div>
          <div className="col-xl-4 col-lg-4">
            <div className="message-sticker position-relative">
              <img src="assets/img/bg/message-sticker.png" alt="img" />
              <div className="etablist-price">
                <p>
                  {" "}
                  Mais de <span>100</span> Aprovações
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UniversityMessage;
