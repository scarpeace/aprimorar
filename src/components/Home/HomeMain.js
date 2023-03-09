import React from "react";
import CampusSection from "./BottomSection/CampusSection";
import CounterSection from "./MiddleSection/CounterSection";
import EventSection from "./BottomSection/EventSection";
import MainBannerSection from "./MainBanner/MainBannerSection";
import UniversityCardSection from "./MainBanner/BannerCardsSection";
import UniversityMessage from "./MiddleSection/UniversityMessage";

const HomeMain = (props) => {
  const { uniCards } = props;

  return (
    <main>
      <MainBannerSection />
      <UniversityCardSection uniCards={uniCards} />
      <UniversityMessage />
      <CounterSection />
      <CampusSection />
      <EventSection />
    </main>
  );
};

export default HomeMain;
