import React from "react";

const AboutStudentChoose = () => {
  return (
    <div className="student-choose-area fix pt-70 pb-60">
      <div className="container">
        <div className="row">
          <div className="col-xl-5 col-lg-5">
            <div className="student-wrapper mb-30">
              <div className="section-title mb-30">
                <h2>
                  Why Students <span className="down-mark-line">Choose</span> Us
                  for Gain Their Knowledge
                </h2>
              </div>
              <div className="sitdent-choose-content">
                <p>
                  Helping employees gain skills and providing career development
                  often take a back seat to business priorities but workplace
                  better right now. Seventy percent of workers think that.
                </p>
              </div>
              <div className="student-choose-list">
                <ul>
                  <li>
                    <i className="far fa-check"></i>Scratch to HTML
                  </li>
                  <li>
                    <i className="far fa-check"></i>Learn how to code in Python
                  </li>
                  <li>
                    <i className="far fa-check"></i>Unlimited backend database
                    creation
                  </li>
                  <li>
                    <i className="far fa-check"></i>Adobe XD Tutorials
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div className="col-xl-2 col-lg-2">
            <div className="student-wrapper position-relative">
              <div className="shap-01"></div>
            </div>
          </div>
          <div className="col-xl-5 col-lg-5">
            <div className="student-choose-wrapper position-relative mb-30">
              <div className="shap-02"></div>
              <div className="shap-03">
                <img
                  src="assets/img/shape/student-shape-03.png"
                  alt="image not found"
                />
              </div>
              <div className="shap-04">
                <img
                  src="assets/img/shape/student-shape-04.png"
                  alt="image not found"
                />
              </div>
              <div className="shap-05">
                <img
                  src="assets/img/shape/student-shape-05.png"
                  alt="image not found"
                />
              </div>
              <div className="shap-06">
                <img
                  src="assets/img/shape/student-shape-06.png"
                  alt="image not found"
                />
              </div>
              <div className="shap-07">
                <img
                  src="assets/img/shape/student-shape-07.png"
                  alt="image not found"
                />
              </div>

              <div className="student-choose-thumb">
                <img
                  src="assets/img/student-choose/student.png"
                  alt="image not found"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AboutStudentChoose;
