-- phpMyAdmin SQL Dump
-- version 4.5.3.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 08. Jun 2016 um 17:31
-- Server-Version: 5.6.26
-- PHP-Version: 5.5.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `igtss16`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `costumer`
--

CREATE TABLE `costumer` (
  `costumer_id` int(11) NOT NULL,
  `lastname` varchar(220) NOT NULL DEFAULT '',
  `firstname` varchar(220) NOT NULL DEFAULT '',
  `birthday` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `costumer_peer_group`
--

CREATE TABLE `costumer_peer_group` (
  `costumer_id` int(11) NOT NULL,
  `peer_group_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `igt_order`
--

CREATE TABLE `igt_order` (
  `igt_order_id` int(11) NOT NULL,
  `costumer_id` int(11) NOT NULL,
  `value` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `kpi`
--

CREATE TABLE `kpi` (
  `id` int(11) NOT NULL,
  `found` int(11) NOT NULL,
  `relevant` int(11) NOT NULL,
  `intersection_found_relevant` int(11) NOT NULL,
  `resource_name` varchar(255) NOT NULL,
  `step_name` varchar(220) NOT NULL,
  `kpi_f_measure` decimal(4,3) NOT NULL,
  `kpi_recall` decimal(4,3) NOT NULL,
  `kpi_precision` decimal(4,3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `method`
--

CREATE TABLE `method` (
  `id` int(11) NOT NULL,
  `name` varchar(220) NOT NULL,
  `type` enum('GET','POST','PUT','DELETE','UPDATE') NOT NULL,
  `resource_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `peer_group`
--

CREATE TABLE `peer_group` (
  `peer_group_id` int(11) NOT NULL,
  `costumer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `process`
--

CREATE TABLE `process` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `resource`
--

CREATE TABLE `resource` (
  `id` int(11) NOT NULL,
  `path` varchar(220) DEFAULT NULL,
  `servlet_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `servlet`
--

CREATE TABLE `servlet` (
  `id` int(11) NOT NULL,
  `base_url` varchar(220) NOT NULL,
  `path` varchar(220) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `step`
--

CREATE TABLE `step` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `process_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `costumer`
--
ALTER TABLE `costumer`
  ADD PRIMARY KEY (`costumer_id`),
  ADD KEY `costumer_id` (`costumer_id`);

--
-- Indizes für die Tabelle `costumer_peer_group`
--
ALTER TABLE `costumer_peer_group`
  ADD PRIMARY KEY (`costumer_id`,`peer_group_id`),
  ADD KEY `peer_group_peer_group_con` (`peer_group_id`);

--
-- Indizes für die Tabelle `igt_order`
--
ALTER TABLE `igt_order`
  ADD PRIMARY KEY (`igt_order_id`),
  ADD KEY `igt_order_id` (`igt_order_id`),
  ADD KEY `costumer_id` (`costumer_id`);

--
-- Indizes für die Tabelle `kpi`
--
ALTER TABLE `kpi`
  ADD PRIMARY KEY (`id`),
  ADD KEY `process_id` (`resource_name`,`step_name`),
  ADD KEY `kpi_step` (`step_name`);

--
-- Indizes für die Tabelle `method`
--
ALTER TABLE `method`
  ADD PRIMARY KEY (`id`),
  ADD KEY `webservice_id` (`resource_id`);

--
-- Indizes für die Tabelle `peer_group`
--
ALTER TABLE `peer_group`
  ADD PRIMARY KEY (`peer_group_id`);

--
-- Indizes für die Tabelle `process`
--
ALTER TABLE `process`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indizes für die Tabelle `resource`
--
ALTER TABLE `resource`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `path` (`path`),
  ADD KEY `webservile_id` (`servlet_id`);

--
-- Indizes für die Tabelle `servlet`
--
ALTER TABLE `servlet`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `base_url` (`base_url`,`path`);

--
-- Indizes für die Tabelle `step`
--
ALTER TABLE `step`
  ADD PRIMARY KEY (`id`),
  ADD KEY `process_id` (`process_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `costumer`
--
ALTER TABLE `costumer`
  MODIFY `costumer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT für Tabelle `igt_order`
--
ALTER TABLE `igt_order`
  MODIFY `igt_order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT für Tabelle `kpi`
--
ALTER TABLE `kpi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=178;
--
-- AUTO_INCREMENT für Tabelle `method`
--
ALTER TABLE `method`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=107;
--
-- AUTO_INCREMENT für Tabelle `peer_group`
--
ALTER TABLE `peer_group`
  MODIFY `peer_group_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT für Tabelle `process`
--
ALTER TABLE `process`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
--
-- AUTO_INCREMENT für Tabelle `resource`
--
ALTER TABLE `resource`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=123;
--
-- AUTO_INCREMENT für Tabelle `servlet`
--
ALTER TABLE `servlet`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;
--
-- AUTO_INCREMENT für Tabelle `step`
--
ALTER TABLE `step`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `costumer_peer_group`
--
ALTER TABLE `costumer_peer_group`
  ADD CONSTRAINT `costumer_costumer_con` FOREIGN KEY (`costumer_id`) REFERENCES `costumer` (`costumer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `peer_group_peer_group_con` FOREIGN KEY (`peer_group_id`) REFERENCES `peer_group` (`peer_group_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `igt_order`
--
ALTER TABLE `igt_order`
  ADD CONSTRAINT `igt_order_costumer_cons` FOREIGN KEY (`costumer_id`) REFERENCES `costumer` (`costumer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `method`
--
ALTER TABLE `method`
  ADD CONSTRAINT `method_webservice` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `resource`
--
ALTER TABLE `resource`
  ADD CONSTRAINT `resource_servlet` FOREIGN KEY (`servlet_id`) REFERENCES `servlet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `step`
--
ALTER TABLE `step`
  ADD CONSTRAINT `step_process` FOREIGN KEY (`process_id`) REFERENCES `process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
