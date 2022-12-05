package com.example.tasbd.controller;
import java.util.List;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.tasbd.model.Kaset;
import com.example.tasbd.model.Perental;
import com.example.tasbd.model.Peminjam;
import com.example.tasbd.model.Login;

@Controller
public class PinjamController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getUser() {
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String login(@ModelAttribute(name = "Login") Login login, Model model) {
        String email = login.getEmail();
        String password = login.getPassword();
        try {
            String sql = "SELECT * FROM login WHERE email = ?";
            Login asli = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Login.class), email);
            model.addAttribute("asli", asli);
            String emailAsli = asli.getEmail();
            String passAsli = asli.getPassword();
            if (password.equals(passAsli)) {
                return "redirect:/index";
            }
        } catch (EmptyResultDataAccessException e) {
            // TODO: handle exception
            model.addAttribute("invalidCredentials", true);
        }
        model.addAttribute("invalidCredentials", true);
        return "login";
    }

    @GetMapping("/index")
    public String index(Model model) {
        String sql = "SELECT * FROM KASET WHERE DELETED = 'N'";
        List<Kaset> kaset = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Kaset.class));
        model.addAttribute("kaset", kaset);
        return "index";
    }

    @GetMapping("/dataperental")
    public String dataperental(Model model) {
        String sql = "SELECT * FROM PERENTAL";
        List<Perental> perental = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Perental.class));
        model.addAttribute("perental", perental);
        return "dataperental";
    }
    @GetMapping("/datapeminjam/{id_kaset}")
    public String datapeminjam(@PathVariable("id_kaset") String id_kaset, Model model) {
        String sql = "SELECT * FROM PEMINJAM WHERE ID_KASET=?";
        List<Peminjam> peminjam = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Peminjam.class), id_kaset);
        model.addAttribute("peminjam", peminjam);
        return "datapeminjam";
    }

    @GetMapping("/addkaset")
    public String addkaset(Model model) {
        return "addkaset";
    }

    @RequestMapping(value ="/addkaset", method = RequestMethod.POST)
    public String addkaset(Kaset kaset, Model model) {

        try {
            String sql = "INSERT INTO KASET VALUES (?, ?, ?, ?, 'N')";
            jdbcTemplate.update(sql, kaset.getid_kaset(), kaset.getjudul(), kaset.getgenre(), kaset.gettahun_tayang());
            return "redirect:/index";
        } catch (Exception e) {
            // TODO: handle exception
            model.addAttribute("invalidID", true);
        }
        model.addAttribute("invalidID", true);
        return "redirect:/index";

    }

    @GetMapping("/softdeletekaset/{id_kaset}")
    public String softDelete(@PathVariable("id_kaset") String id_kaset) {
        String sql = "UPDATE kaset SET DELETED = 'Y' WHERE id_kaset = ?";
        jdbcTemplate.update(sql, id_kaset);
        return "redirect:/index";
    }

    @GetMapping("/deletepeminjam/{id_peminjam}")
    public String deletepeminjam(@PathVariable("id_peminjam") String id_peminjam) {
        String sql = "DELETE FROM peminjam WHERE id_peminjam = ?";
        jdbcTemplate.update(sql, id_peminjam);
        return "redirect:/index";
    }

    @GetMapping("/harddeleteperental/{id_perental}")
    public String harddeleteperental(@PathVariable("id_perental") String id_perental) {
        String sql = "DELETE FROM perental WHERE ID_perental= ?";
        jdbcTemplate.update(sql, id_perental);
        return "redirect:/dataperental";
    }
    @GetMapping("/harddeleteriwayat/{id_kaset}")
    public String harddeleteriwayat(@PathVariable("id_kaset") String id_kaset) {
        String sql = "DELETE FROM kaset WHERE id_kaset = ?";
        jdbcTemplate.update(sql, id_kaset);
        return "redirect:/index";
    }
    @GetMapping("/searchid")
    public String search(@PathParam("judul") String judul, Model model) {
        String sql = "SELECT * FROM kaset WHERE judul LIKE CONCAT(CONCAT ('%', ?), '%')";
        List<Kaset> kasetList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Kaset.class), judul);
        model.addAttribute("kaset", kasetList);
        return ("searchid");
    }

    @GetMapping("/addperental")
    public String addperental(Model model) {
        return "addperental";
    }

    @RequestMapping(value ="/addperental", method = RequestMethod.POST)
    public String addperental(Perental perental, Model model) {

        try {
            String sql = "INSERT INTO PERENTAL VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, perental.getid_perental(), perental.getnama_perental(), perental.getnotelp_perental(), perental.getalamat_perental(), perental.getid_kaset());
            return "redirect:/dataperental";
        } catch (Exception e) {
            // TODO: handle exception
            model.addAttribute("invalidID", true);
        }
        model.addAttribute("invalidID", true);
        return "redirect:/dataperental";

    }

    @GetMapping("/addpeminjam")
    public String addpeminjam(Model model) {
        return "addpeminjam";
    }

    @PostMapping(value ="/addpeminjam")
    public String addpeminjam(Peminjam peminjam, Model model) {

        String sql = "INSERT INTO PEMINJAM VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, peminjam.getid_peminjam(), peminjam.getnama_peminjam(), peminjam.getnotelp_peminjam(), peminjam.getalamat_peminjam(), peminjam.getketerangan(), peminjam.getid_kaset());
        return "redirect:/index";

    }
    @GetMapping("/editkaset/{id_kaset}")
    public String editkaset(@PathVariable("id_kaset") String id_kaset, Model model) {
        String sql = "SELECT * FROM kaset WHERE id_kaset = ?";
        Kaset kaset = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Kaset.class), id_kaset);
        model.addAttribute("kaset", kaset);
        return "editkaset";
    }

    @PostMapping("/editkaset")
    public String editkaset(Kaset kaset) {
        String sql = "UPDATE kaset SET id_kaset=?, judul = ?, genre = ?, tahun_tayang = ? WHERE id_kaset = ?";
        jdbcTemplate.update(sql, kaset.getid_kaset(), kaset.getjudul(), kaset.getgenre(), kaset.gettahun_tayang(), kaset.getid_kaset());
        return "redirect:/index";
    }

    @GetMapping("/riwayat")
    public String riwayat(Model model) {
        String sql = "SELECT * FROM kaset WHERE DELETED='Y'";
        List<Kaset> kasetList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Kaset.class));
        model.addAttribute("kaset", kasetList);
        return "riwayat";
    }
    @GetMapping("/detailkaset/{id_kaset}")
    public String detailkaset(@PathVariable("id_kaset") String id_kaset, Model model) {
        String sql1 = "SELECT * FROM kaset WHERE id_kaset = ?";
        String sql2 = "SELECT * FROM perental WHERE id_kaset = ?";
        Kaset kaset = jdbcTemplate.queryForObject(sql1,
                BeanPropertyRowMapper.newInstance(Kaset.class), id_kaset);
        Perental perental = jdbcTemplate.queryForObject(sql2,
                BeanPropertyRowMapper.newInstance(Perental.class), id_kaset);
        model.addAttribute("kaset", kaset);
        model.addAttribute("perental", perental);
        return "detailkaset";
    }
    @GetMapping("/restore/{id_kaset}")
    public String restore(@PathVariable("id_kaset") String id_kaset) {
        String sql = "UPDATE kaset SET DELETED = 'N' WHERE id_kaset = ?";
        jdbcTemplate.update(sql, id_kaset);
        return "redirect:/index";
    }
}
