import React from "react";
import Breadcrumb from "../Common/Breadcrumb";
import ContactFormSection from "./ContactFormSection";
import ContactMap from "./ContactMap";
import ContactSidebar from "./ContactSidebar";

const ContactMain = () => {
  return (
    <main>
      <Breadcrumb breadcrumbTitle="Contact" breadcrumbSubTitle="Contact Us" />

      <div className="contact-area pt-120 pb-90">
        <div className="container">
          <div className="row">
            <div className="col-xl-8 col-lg-7 col-md-12">
              <div className="contact-area-wrapper">
                <div className="section-title mb-50">
                  <h2>Entre em Contato</h2>
                </div>
                <ContactFormSection />
                <ContactMap />
              </div>
            </div>
            <div className="col-xl-4 col-lg-5 col-md-8">
              <ContactSidebar />
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

export default ContactMain;
