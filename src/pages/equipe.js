import Head from "next/head";
import InstructorMain from "../components/Instructor/InstructorMain";
import Footer from "../components/Layout/Footer/Footer";
import Header from "../components/Layout/Header/Header";

export default function Instructor() {
  return (
    <>
      <Head>
        <title>
          Eduman - Education And Online Courses React, NextJs Template
        </title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <Header />
      <InstructorMain />
      <Footer />
    </>
  );
}